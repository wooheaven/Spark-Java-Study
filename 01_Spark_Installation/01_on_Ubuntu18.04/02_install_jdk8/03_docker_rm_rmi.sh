container=`docker ps -a | grep 18.04_2nd_jdk8 | awk '{print $1}'`
if [ $container ] ; then
docker stop $container
docker rm $container
docker ps -a
fi
unset container

image=`docker images | grep 18.04_2nd_jdk8 | awk '{print $3}'`
if [ $image ] ; then
docker rmi $image
docker images
fi
unset image

image=`docker images | grep "<none>" | awk '{print $3}'`
if [ $image ] ; then
echo ""
echo "docker image ["$image"] will be removed"
echo ""
docker rmi $image
docker images
fi
unset image
