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
    sshpass -p "$password" ssh swarch@"$client" "
    cd Documents/JFCandJCR &&
    for i in {1..4}
    do
        java -jar client.jar rff rff.txt 24000&
    done

    exit 0
    "&
done
wait

exit