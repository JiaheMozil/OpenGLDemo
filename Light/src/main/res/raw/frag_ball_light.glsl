precision mediump float;
uniform float uR;
uniform vec3 uLightLocation;
uniform mat4 uMMatrix;
uniform vec3 uCamera;
varying vec3 vPosition;
varying vec3 vNormal;

uniform sampler2D uTextureUnit;
varying vec2 vTextureCoordinates;
//返回散射光强度
vec4 diffuseLight(vec3 normal,vec3 lightLocation,vec4 lightDiffuse){
    //变换后的法向量
    vec3 newTarget = normalize((uMMatrix * vec4(normal + vPosition,1)).xyz-(uMMatrix * vec4(vPosition,1)).xyz);
    //表面点与光源的方向向量
    vec3 vp = normalize(lightLocation - (uMMatrix * vec4(vPosition,1)).xyz);

    return lightDiffuse * max(0.0, dot(newTarget,vp));
}

vec4 specularLight(vec3 normal,vec3 lightLocation,vec4 lightSpecular){
    //变换后的法向量
    vec3 transformedNormal = normalize((uMMatrix * vec4(normal + vPosition,1)).xyz-(uMMatrix * vec4(vPosition,1)).xyz);

    //表面点与光源的方向向量
    vec3 normalizedLightDirection = normalize(lightLocation - (uMMatrix * vec4(vPosition,1)).xyz);

    //表面点与眼睛的方向向量
    vec4 eyeVertexPosition = uMMatrix * vec4(vPosition, 1.0);
    vec3 eyeVector = normalize(uCamera - eyeVertexPosition.xyz);

    vec3 halfVector = normalize(normalizedLightDirection + eyeVector);

    float specularStrength = dot(halfVector, transformedNormal);
    float smoothness = 50.0;
    specularStrength = pow(specularStrength, smoothness);

    return lightSpecular * max(0.0, specularStrength);
}

/*void pointLight(in vec3 normal,inout vec4 ambient,inout vec4 diffuse,
  inout vec4 specular,
  in vec3 lightLocation,
  in vec4 lightAmbient,
  in vec4 lightDiffuse,
  in vec4 lightSpecular
){
  ambient=lightAmbient;
  vec3 normalTarget=vPosition+normal;
  vec3 newNormal=(uMMatrix*vec4(normalTarget,1)).xyz-(uMMatrix*vec4(vPosition,1)).xyz;
  newNormal=normalize(newNormal);
  vec3 eye= normalize(uCamera-(uMMatrix*vec4(vPosition,1)).xyz);

  vec3 vp= normalize(lightLocation-(uMMatrix*vec4(vPosition,1)).xyz);
  vp=normalize(vp);
  vec3 halfVector=normalize(vp+eye);
  float shininess=50.0;
  float nDotViewPosition=max(0.0,dot(newNormal,vp));
  diffuse=lightDiffuse*nDotViewPosition;
  float nDotViewHalfVector=dot(newNormal,halfVector);
  float powerFactor=max(0.0,pow(nDotViewHalfVector,shininess));
  specular=lightSpecular*powerFactor;
}*/
void main()
{
   /*vec3 color;
   float n = 8.0;
   float span = 2.0*uR/n;
   int i = int((vPosition.x + uR)/span);
   int j = int((vPosition.y + uR)/span);
   int k = int((vPosition.z + uR)/span);
   int whichColor = int(mod(float(i+j+k),2.0));
   if(whichColor == 1) {
        color = vec3(0.678,0.231,0.129);
   }
   else {
        color = vec3(1.0,1.0,1.0);
   }
   vec4 finalColor=vec4(color,0);*/

   vec4 finalColor = texture2D(uTextureUnit,vTextureCoordinates);
   vec4 ambient,diffuse,specular;
   ambient = vec4(0.15,0.15,0.15,1.0);
   diffuse = diffuseLight(normalize(vNormal),uLightLocation,vec4(0.8,0.8,0.8,1.0));
   specular = specularLight(normalize(vNormal),uLightLocation,vec4(0.7,0.7,0.7,1.0));
//   pointLight(normalize(vNormal),ambient,diffuse,specular,uLightLocation,
//   vec4(0.15,0.15,0.15,1.0),vec4(0.8,0.8,0.8,1.0),vec4(0.7,0.7,0.7,1.0));
   gl_FragColor=finalColor*ambient + finalColor*diffuse + finalColor*specular;
}     