password=$(<../credentials/password.txt)

server_ip="hgrid5" ## Definir donde pondremos el servidor
clients=("hgrid7")


sshpass -p "$password" ssh swarch@"$server_ip" "
    cd Documents/JuanCRamirezAndJuanFCastillo &&
    java -jar server.jar
" &

for client in "${clients[@]}"; do
    sshpass -p "$password" ssh swarch@"$client" "
    cd Documents/JuanCRamirezAndJuanFCastillo &&
    java -jar server.jar &&
    24
    "&
done

wait

exit