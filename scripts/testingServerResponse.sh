password=$(<../credentials/password.txt)

clients=("xhgrid7")

for client in "${clients[@]}"; do
    sshpass -p "$password" ssh swarch@"$client" "
    cd Documents/JuanCRamirezAndJuanFCastillo &&
    while true; do
        echo '100' # Comando a ejecutar infinitamente
        
        sleep 1  # ajusta el intervalo de tiempo entre cada envío
        
        ssh -o BatchMode=yes -o ConnectTimeout=5 swarch@"$client" exit
        if [ $? -ne 0 ]; then
            break  
        fi
    done | java -jar client.jar
    "
done

wait
exit