package com.android.mozil.openglutil.util


object Geometry {


    class Point(val x: Float, val y: Float, val z: Float) {

        fun translateY(distance: Float): Point = Point(x, y + distance, z)

        fun translate(vector: Vector): Point = Point(x + vector.x, y + vector.y, z + vector.z)
    }


    class Circle(val center: Point, val radius: Float) {
        fun scale(scale: Float): Circle = Circle(center, radius * scale)
    }


    class Cylinder(val center: Point, val radius: Float, val height: Float)


    class Ray(val point: Point, val vector: Vector)


    class Vector(val x: Float, val y: Float, val z: Float) {

        fun length(): Float = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()

        /**
         * @param other
         * @return
         */
        fun crossProduct(other: Vector): Vector = Vector(
                    y * other.z - z * other.y,
                    z * other.x - x * other.z,
                    x * other.y - y * other.x)

        /**
         * * @param other
         * @return
         */
        fun dotProduct(other: Vector): Float = (x * other.x
                    + y * other.y
                    + z * other.z)

        /**
         * @param f
         */
        fun scale(f: Float): Vector = Vector(x * f, y * f, z * f)
    }

    class Sphere(val center: Point, val radius: Float)

    fun vectorBetween(from: Point, to: Point): Vector = Vector(to.x - from.x, to.y - from.y, to.z - from.z)

    fun intersects(sphere: Sphere, ray: Ray): Boolean = distanceBetween(sphere.center, ray) < sphere.radius

    private fun distanceBetween(point: Point, ray: Ray): Float {
        val p1ToPoint = vectorBetween(ray.point, point)
        val p2ToPoint = vectorBetween(ray.point.translate(ray.vector), point)
        val areaOfTriangleTimesTwo = p1ToPoint.crossProduct(p2ToPoint).length()
        val lengthOfBase = ray.vector.length()
        return areaOfTriangleTimesTwo / lengthOfBase
    }

    class Plane(val point: Point, val normal: Vector)

    fun intersectionPoint(ray: Ray, plane: Plane): Point {

        val rayToPlaneVector = vectorBetween(ray.point, plane.point)
        val scaleFactor = rayToPlaneVector.dotProduct(plane.normal) / ray.vector.dotProduct(plane.normal)
        return ray.point.translate(ray.vector.scale(scaleFactor))
    }
}
