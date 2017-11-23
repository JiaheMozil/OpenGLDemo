uniform mat4 uMatrix;
attribute vec3 aPosition;
varying vec3 vPosition;

void main() {
    vPosition = aPosition;    //把顶点位置传给片段着色器

    vPosition.z = -vPosition.z; //反转Z分量。把右手坐标系转化为左手坐标系

    gl_Position = uMatrix * vec4(aPosition, 1.0);//成u_Matrix即用投影~
    gl_Position = gl_Position.xyww;//把Z值变成W，这样透视除法之后为1，即Z始终在1的远平面上。Z=1最远，即在别的物体的后面，就像是背景。
}
