#!/bin/bash

PROJECT_FILE="${PROJECT_FILE:-project.yml}"
API_FILE=$(yaml read "${PROJECT_FILE}" metadata.apifile)


get_specs() {
    local data

    data=$(yaml -j read project.yml metadata.specs[*] | jq -r '.[] | [ .["target"] ] | join(" ")')
    echo "$data"
}

has_defs() {
    local spec="$1"
    local data

    data=$(yaml read "$spec" definitions)
    if [[ -z "$data" ]] || [[ "$data" == 'null' ]]; then
        echo "false"
    else
        echo "true"
    fi
}

has_paths() {
    local spec="$1"
    local data

    data=$(yaml read "$spec" paths)
    if [[ -z "$data" ]] || [[ "$data" == 'null' ]]; then
        echo "false"
    else
        echo "true"
    fi
}

add_public_defs() {
    local spec="$1"
    if [[ $(has_defs "$spec") == "false" ]]; then
        return
    fi

    local data
    echo -e "\\tadding public defs from $spec"

    data=$(yaml -j read "$spec" definitions | jq 'with_entries( select( .value."x-visibility" == "public" ) ) | {definitions: .}')
    echo "$data" | yaml merge -i "${API_FILE}" -
}

add_public_paths() {
    local spec="$1"
    if [[ $(has_paths "$spec") == "false" ]]; then
        return
    fi

    local data
    echo -e "\\tadding public paths from $spec"

    data=$(yaml -j read "$spec" paths | jq 'with_entries( select ( .value | .[]."x-visibility"? //empty | contains("public")) ) | to_entries | [(.[] | { key: .key, value: (({parameters: .value.parameters}) + (.value | with_entries( select ( .value | ."x-visibility"? //empty | contains("public")))))})] | from_entries | {paths: .}')
    echo "$data" | yaml merge -i "${API_FILE}" -
}

add_spec() {
    local spec="$1"

    if [[ $(has_defs "$spec") == "false" ]] && [[ $(has_paths "$spec") == "false" ]]; then
        cat "$spec" >> "${API_FILE}"
        return
    fi

    add_public_defs "$spec"
    add_public_paths "$spec"
}

merge() {
    echo "==> Merging specs..."

    rm -f "${API_FILE}"
    touch "${API_FILE}"
    chmod 644 "${API_FILE}"

    mapfile -t specs < <(get_specs)
    for spec in "${specs[@]}"; do
        echo "processing spec: $spec"
        if [[ $spec =~ 'http' ]]; then
            rm -f /tmp/api.yaml
            wget -O /tmp/api.yaml "$spec"
            spec=/tmp/api.yaml
        fi
        add_spec "$spec"
    done
}

merge
