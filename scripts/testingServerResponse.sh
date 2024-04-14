password=$(<../credentials/password.txt)

<<<<<<< HEAD
clients=("hgrid6" "hgrid7" "hgrid8" "hgrid9" "hgrid10"
                "hgrid11" "hgrid13" "hgrid14" "hgrid17")

=======
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
>>>>>>> 5bc7721ad08f70f9e2f514600dbfdd59a55926b1

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