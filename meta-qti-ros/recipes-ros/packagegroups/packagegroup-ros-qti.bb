DESCRIPTION = "ros-world package group"
LICENSE = "MIT"

inherit packagegroup

PACKAGES = "${PN}"

RDEPENDS_${PN} = "\
    packagegroup-ros-comm \
    actionlib \
    bond \
    bondcpp \
    bondpy \
    smclib \
    class-loader \
    actionlib-msgs \
    diagnostic-msgs \
    nav-msgs \
    geometry-msgs \
    sensor-msgs \
    shape-msgs \
    stereo-msgs \
    trajectory-msgs \
    visualization-msgs \
    dynamic-reconfigure \
    tf2 \
    tf2-msgs \
    tf2-py \
    tf2-ros \
    tf \
    image-transport \
    nodelet \
    pluginlib \
    cmake-modules \
    rosconsole-bridge \
    ros-scripts \
    camera-info-manager \
    python-rosdep \
    python-json \
    git \
    python-empy \
    rospy-tutorials \
    pcl \
"

# kdl-parser-py requires python-orocos-kdl, which current fails due to #469.
# urdfdom-headers is an empty deploy package.
# image-view requires gtk+, but it cannot be found by cmake for some reason.
# sound-play requires python-gst (which is not available in any layers' master branch)
# joint-state-publisher requires opengl distro feature and has further issues building.
# freenect-camera and freenect-launch requires opengl distro feature.
