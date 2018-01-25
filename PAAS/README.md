cd PAAS

oc cluster up

oc policy add-role-to-user view system:serviceaccount:$(oc project -q):default -n $(oc project -q)

cd paas-web

mvn -Popenshift clean fabric8:deploy

cd ../paas-ai

mvn -Popenshift clean fabric8:deploy


pumba --random --interval 1m kill --signal SIGKILL re2:.*paas.*

pumba netem --duration 5m --interface eth0 delay \
      --time 3000 \
      --jitter 30 \
      --correlation 20 \
    re2:.*paas.*
