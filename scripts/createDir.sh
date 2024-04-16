password=$(<../credentials/password.txt)

devices=("hgrid5" "hgrid6" "hgrid7" "hgrid8" "hgrid9" "hgrid10" 
"hgrid11" "hgrid12" "hgrid13" "hgrid14" "hgrid15" "hgrid16" "hgrid17"
"hgrid18" "hgrid19" "hgrid20" "hgrid21" "hgrid22")

for device in "${devices[@]}"
do
    sshpass -p "$password" ssh swarch@"$device" 'mkdir Documents/JFCandJCR'
done

