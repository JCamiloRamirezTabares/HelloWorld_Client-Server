password=$(<../credentials/password.txt)

devices=("hgrid6" "hgrid7" "hgrid8" "hgrid9" "hgrid10")

for device in "${devices[@]}"
do
    ssh swarch@"$device" 'mkdir Documents/JFCandJCR'
done

