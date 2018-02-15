# Deployment

## Launch script

```
#!/bin/sh

ps auxw | grep quickstart.jar | grep -v grep > /dev/null

if [ $? != 0 ]
then
    cd ~/webapps/re_frame_quickstart
    java -jar quickstart.jar --port <PORT>
fi
```

## Crontab line

```
8 */1 * * * ~/webapps/re_frame_quickstart/launch.sh
```

Source: https://www.digitalocean.com/community/tutorials/how-to-use-a-simple-bash-script-to-restart-server-programs
