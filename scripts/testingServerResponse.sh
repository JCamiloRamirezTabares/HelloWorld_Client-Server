password=$(<../credentials/password.txt)

# Definir los servidores y sus respectivos números de Fibonacci
declare -A client_fib_numbers=(
    ["hgrid6"]=46
    ["hgrid7"]=30
    ["hgrid8"]=10
    ["hgrid9"]=7
    ["hgrid10"]=25
    ["hgrid11"]=40
    ["hgrid12"]=46
    ["hgrid13"]=30
    ["hgrid15"]=25
)

# Bucle sobre cada par clave-valor en el diccionario
for client in "${!client_fib_numbers[@]}"; do
    fibonacci_number=${client_fib_numbers[$client]}
    sshpass -p "$password" ssh swarch@"$client" "
    cd Documents/JFCandJCR &&
    {
        echo $fibonacci_number
        echo exit
    } | java -jar client.jar
    "&
done
wait

exit