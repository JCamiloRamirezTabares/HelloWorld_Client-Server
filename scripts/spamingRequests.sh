password=$(<../credentials/password.txt)

clients=1
requests_per_client=250

client="xhgrid6"

for i in $(seq 1 $clients)
do
  sshpass -p "$password" ssh swarch@"$client" "
  cd Documents/JFCandJCR &&
  java -jar client.jar spm $requests_per_client" 45 &
  sleep 65
done

wait
exit 0