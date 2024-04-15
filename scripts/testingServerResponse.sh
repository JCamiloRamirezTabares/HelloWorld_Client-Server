password=$(<../credentials/password.txt)

# Definir los servidores y sus respectivos números de Fibonacci
declare -A client_fib_numbers=(
    ["hgrid6"]=49
    ["hgrid7"]=30
    ["hgrid8"]=10
    ["hgrid9"]=7
    ["hgrid10"]=25
    ["hgrid11"]=40
    ["hgrid12"]=55
    ["hgrid13"]=30
    ["hgrid15"]=21
)

# Bucle sobre cada par clave-valor en el diccionario
for client in "${!client_fib_numbers[@]}"; do
    fibonacci_number=${client_fib_numbers[$client]}
    ssh swarch@"$client" "
    cd Documents/JFCandJCR &&
    {
        echo $fibonacci_number
        echo 15
        sleep 1
        echo 5
        sleep 120
        echo exit
    } | java -jar client.jar
    "&
done
wait

exit