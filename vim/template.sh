#!/bin/bash
# Short description

readonly PROGNAME=$(basename "$0")
readonly PROGDIR=$(dirname "$(readlink -m "$0")")
readonly -a ARGS=("$@")

printUsage() {
    cat <<EOF
usage: $PROGNAME <arg>
EOF
}

# Always use declare instead of local!
# It's more general, can do more, and leads to more consistency.

readonly TEMPDIR=$(mktemp -d /tmp/tmp.XXXXXXXXXX)
function finish {
    rm -rf "$TEMPDIR"
}
trap finish EXIT

# $1: error message
exitWithError() {
    echo "$1" >&2
    exit 1
}

# $*: command line arguments = "$@"
parseCommandLine() {
    declare arg
    # this syntax iterates over all function args
    for arg; do
        declare delim=""
        case "$arg" in
            # translate --gnu-long-options to -g (short options)
            --config)         args="${args}-c " ;;
            --pretend)        args="${args}-n " ;;
            --test)           args="${args}-t " ;;
            --help-config)    usage_config && exit 0 ;;
            --help)           args="${args}-h " ;;
            --verbose)        args="${args}-v " ;;
            --debug)          args="${args}-x " ;;
            # pass through anything else
            *) [[ "${arg:0:1}" == "-" ]] || delim="\""
                args="${args}${delim}${arg}${delim} " ;;
        esac
    done

    # Reset the positional parameters to the short options
    eval set -- $args

    # declare options globally and readonly
    while getopts "nvhxt:c:" OPTION; do
         case $OPTION in
         v)
             declare -gr VERBOSE=1
             ;;
         h)
             usage
             exit 0
             ;;
         x)
             declare -gr DEBUG='-x'
             set -x
             ;;
         t)
             RUN_TESTS=$OPTARG
             verbose VINFO "Running tests"
             ;;
         c)
             declare -gr CONFIG_FILE=$OPTARG
             ;;
         n)
             declare -gr PRETEND=1
             ;;
        esac
    done
    shift $((OPTIND-1))

    if [[ -z $RUN_TESTS ]]; then
        [[ ! -f $CONFIG_FILE ]] \
            && exitWithError "You must provide --config file"
    fi


    if [[ $# != 1 ]]; then
        exitWithError "$(printUsage)"
    fi

    return 0
}


main() {
    parseCommandLine "${ARGS[@]}"
    false
}

main
