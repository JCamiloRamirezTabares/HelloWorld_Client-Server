password=$(<../credentials/password.txt)

devices=("hgrid5" "hgrid6" "hgrid7" "hgrid8" "hgrid9" "hgrid10" 
"hgrid11" "hgrid12" "hgrid13" "xhgrid14" "xhgrid15")

for device in "${devices[@]}"
do
    sshpass -p "$password" ssh swarch@"$device" 'mkdir Documents/JFCandJCR'
done

