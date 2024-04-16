password=$(<../credentials/password.txt)

# Definir los servidores y sus respectivos números de Fibonacci
declare -A client_fib_numbers=(
    ["xhgrid6"]=55
    ["xhgrid7"]=30
    ["xhgrid8"]=10
    ["xhgrid9"]=7
)

# Bucle sobre cada par clave-valor en el diccionario
for client in "${!client_fib_numbers[@]}"; do
    fibonacci_number=${client_fib_numbers[$client]}
    sshpass -p "$password" ssh swarch@"$client" "
    cd Documents/JFCandJCR &&
    {
        echo $fibonacci_number
        sleep 65
        echo exit
    } | java -jar client.jar
    "&
done
wait

exit