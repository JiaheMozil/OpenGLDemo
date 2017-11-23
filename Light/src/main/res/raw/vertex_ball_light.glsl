uniform mat4 uMVPMatrix;
attribute vec3 aPosition;
attribute vec3 aNormal;
varying vec3 vPosition;
varying vec3 vNormal;

attribute vec2 aTextureCoordinates;
varying vec2 vTextureCoordinates;

void main()
{

   gl_Position = uMVPMatrix * vec4(aPosition,1);
   vPosition = aPosition;
   vTextureCoordinates = aTextureCoordinates;
   vNormal = aNormal;
}                      