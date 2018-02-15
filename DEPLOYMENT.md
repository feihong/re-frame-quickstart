# Deployment

## Production config file

prod-config.edn

```
{:port 39422
 :app-context "/re-frame-quickstart"}
```

## Launch script

launch.sh

```
#!/bin/sh

ps auxw | grep quickstart.jar | grep -v grep > /dev/null

if [ $? != 0 ]
then
    cd ~/webapps/re_frame_quickstart
    java -Dconf=prod-config.edn -jar quickstart.jar
fi
```

## Crontab line

```
8 */1 * * * ~/webapps/re_frame_quickstart/launch.sh
```

Source: https://www.digitalocean.com/community/tutorials/how-to-use-a-simple-bash-script-to-restart-server-programs
