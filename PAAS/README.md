
oc login -u system:admin
oc adm policy add-cluster-role-to-user cluster-admin developer
oc login -u developer
oc adm policy add-scc-to-user privileged system:serviceaccount:$(oc project -q):default
oc edit scc restricted

allowHostDirVolumePlugin: true
