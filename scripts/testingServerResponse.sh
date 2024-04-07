password=$(<../credentials/password.txt)

clients=("hgrid6" "hgrid7" "hgrid8" "hgrid9" "hgrid10" 
"hgrid11" "hgrid12" "hgrid13" "hgrid14" "hgrid15")

fibonacci_number=70 # El número de Fibonacci que deseas calcular

for client in "${clients[@]}"; do
    sshpass -p "$password" ssh swarch@"$client" "
    cd Documents/JFCandJCR &&
    {
        echo $fibonacci_number
        echo exit
    } | java -jar client.jar &
    "
done

wait
exit