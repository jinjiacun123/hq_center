#tcpdump tcp -i eth0 -t -s 0 and dst port 6379 -w ./target.cap
tcpdump tcp -i eth0 -t -s 0 -w ./target.cap
