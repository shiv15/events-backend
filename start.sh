# start.sh
if [[ $DYNO == "web"* ]]; then
  rails server -p $PORT
elif  [[ $DYNO == "worker"* ]]; then
  sidekiq -q default
fi
