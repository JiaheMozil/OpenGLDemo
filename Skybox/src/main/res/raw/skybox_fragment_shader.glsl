precision mediump float;

uniform samplerCube uTextureUnit;//立方体纹理
varying vec3 vPosition;

void main()
{
    gl_FragColor = textureCube(uTextureUnit, vPosition);
}
