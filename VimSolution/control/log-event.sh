#!/bin/bash
# Short description

readonly PROGNAME=$(basename "$0")
readonly PROGDIR=$(dirname "$(readlink -m "$0")")
readonly -a ARGS=("$@")

readonly TEMP=$(mktemp)

printUsage() {
    cat <<EOF
usage: $PROGNAME [OPTIONS] < state.txt
options:
 -s  seat
 -e  event
EOF
}

# $1: error message
exitWithError() {
    echo "$1" >&2
    exit 1
}

# $*: command line arguments = "$@"
parseCommandLine() {
    # declare options globally and readonly
    while getopts "s:e:h" OPTION; do
         case $OPTION in
         h)
             printUsage
             exit 0
             ;;
         s)
             declare -gr SEAT=$OPTARG
             ;;
         e)
             declare -gr EVENT=$OPTARG
             ;;
        esac
    done
    shift $((OPTIND-1))

    if [[ $# != 0 ]]; then
        exitWithError "$(printUsage)"
    fi

    return 0
}

printAllSeatLetters() {
    tr ' ' '\n' < "$TEMP" | grep 'SEAT---.' | sed -r 's/.*SEAT---(.).*/\1/'
}

# $1: event
# $2: seat
logEvent() {
    echo "$1 $2"
}

main() {
    cat > "$TEMP"

    parseCommandLine "${ARGS[@]}"
    declare event=${EVENT:-update}
    declare seats
    if [[ -n $SEAT ]]; then
        seats=("$SEAT")
    else
        seats=($(printAllSeatLetters))
    fi

    for i in "${seats[@]}"; do
        logEvent "$event" "$i"
    done

    cat "$TEMP"
}

main
