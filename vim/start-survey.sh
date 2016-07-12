#!/bin/bash
# Start seating data collection

readonly PROGNAME=$(basename "$0")
readonly PROGDIR=$(dirname "$(readlink -m "$0")")
readonly -a ARGS=("$@")

printUsage() {
    cat <<EOF
usage: $PROGNAME
EOF
}

readonly WORK_DIR=$PROGDIR/workdir
readonly DATA_DIR=$PROGDIR/data
readonly UI_DIR=$PROGDIR/ui

readonly CSV_SEP=,

# $1: error message
exitWithError() {
    echo "$1" >&2
    exit 1
}

# $1: file to edit
# $2: position cursor at line or regex
vimAsInputDialog() {

    declare file=$1
    declare fileCopy
    fileCopy=$(mktemp)

    declare lineOrRegex=$2

    cp "$file" "$fileCopy"
    vim +"$lineOrRegex" "$file"

    # Return status:
    ! cmp --silent "$file" "$fileCopy" \
        && [[ -s "$file" ]]

}

# $1: filename of survey csv file
printNextId() {
    # First column of survey file must be "id"
    awk -F"$CSV_SEP" 'END{print($1 + 1)}' "$1"
}

# $1: filename of survey csv file
printEventFileName() {
    # First column of survey file must be "id"
    # Scond column of survey file must be "date"
    awk -F"$CSV_SEP" 'END{print("survey_" $2 "_" $1 "_events.csv")}' "$1"
}

# $1: survey form file
# $2: survey data file
prepareSurveyForm() {
    declare surveyFormFile=$1
    declare surveyDataFile=$2

    declare newId todaysDate
    newId=$(printNextId "$surveyDataFile")
    todaysDate=$(date -I)
    sed -i "/^id\b/   s/$/$newId/"      "$surveyFormFile"
    sed -i "/^date\b/ s/$/$todaysDate/" "$surveyFormFile"
}

collectGeneralData() {
    declare generalDataForm=$WORK_DIR/general.txt
    declare surveysDataFile=$DATA_DIR/surveys.csv
    cp "$UI_DIR"/general.txt "$generalDataForm"

    prepareSurveyForm "$generalDataForm" "$surveysDataFile"

    if vimAsInputDialog "$generalDataForm" 3; then

        declare -A data
        declare key value
        while read -r key value; do
            data["$key"]="$value"
        done < "$generalDataForm"

        declare headers dataLine
        headers=$(head -1 "$surveysDataFile" | tr "$CSV_SEP" ' ')
        for i in $headers; do
            dataLine=${dataLine}${CSV_SEP}${data["$i"]}
        done
        # Delete preceding field seperator
        dataLine=${dataLine:1}

        echo "$dataLine" >> "$surveysDataFile"
    else
        exitWithError "Data collection canceled by user."
    fi
}

startSeatingDataCollection() {
    declare stateFile="$WORK_DIR/collection_state.txt"
    cp ui/collection_events.txt "$WORK_DIR/collection_events.txt"
    cp ui/collection_state.txt "$stateFile"

    cd "$PROGDIR" || exitWithError "error: cannot access dir $PROGDIR"

    export SDC_EVENTFILE
    SDC_EVENTFILE=$(printEventFileName data/surveys.csv)
    vim -S control/ui.vim "$stateFile"
}

main() {
    collectGeneralData
    startSeatingDataCollection
}

main
