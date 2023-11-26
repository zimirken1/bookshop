if [ "$#" -eq 0 ]; then
    cat ./bin/usage.txt
    exit 1
fi

output_type="$1"
shift

bundle_name="localization"
out_path="./out"

while getopts "n:o:" opt; do
    case $opt in
        n) bundle_name="$OPTARG";;
        o) out_path="$OPTARG";;
        *) echo "Unknown parameter: -$opt"; exit 1;;
    esac
done

bash ./bin/process_document.sh "$bundle_name"
if [ $? -ne 0 ]; then
  echo "Error while executing process_document.sh"
  exit 1
fi

rm -rf ./out/*
bash ./bin/divide.sh "$output_type" "$out_path" "$bundle_name"