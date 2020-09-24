/* Copyright 2018-20 Richard Oliver. Licensed under Apache Licence version 2.0. */
package ostrat
package geom

/** The purpose of this trait is to provide super trait for surface positions on all Spheroids.  */
trait LatLongBase
{ def latSecs: Double
  def longSecs: Double
  def equatorialRadius: Dist
  def polarRadius: Dist
  @inline final def lat: Latitude = Latitude.secs(latSecs)
  @inline final def long: Longitude = Longitude.secs(longSecs)
  @inline final def latDegs: Double = latSecs.secsToDegs
  @inline final def longDegs: Double = longSecs.secsToDegs
  @inline final def latRadians: Double = latSecs.secsToRadians
  @inline final def longRadians: Double = longSecs.secsToRadians
  @inline final def latMins: Double = latSecs / 60
  @inline final def longMins: Double = longSecs / 60

  override def toString: String = degStr
  def latLetter: String = latRadians.ifNeg("S", "N")
  def longLetter: String = longRadians.ifNeg("W", "E")

  def latDegStr: String = latDegs.abs.str2 + latLetter
  def longDegStr: String = longDegs.abs.str2 + longLetter
  def degStr: String = latDegStr.appendCommas(longDegStr)
  
  def latDegMinStr: String =  
  { val (degs, mins) = latRadians.abs.toDegsMins
    degs.toString + latLetter + mins.str2Dig
  }
  
  def longDegMinStr: String =  
  { val (degs, mins) = longRadians.abs.toDegsMins
    degs.toString + longLetter + mins.str2Dig
  }
   
  def degMinStr: String = latDegMinStr.appendCommas(longDegMinStr)
  def degMinStrs: (String, String) = (latDegMinStr, longDegMinStr)   
  
  def toDist3: Dist3 =
  { val clat = latRadians.cos.abs
    Dist3(longRadians.sin * equatorialRadius * clat, latRadians.sin * polarRadius, longRadians.cos * equatorialRadius * clat)
  }
}