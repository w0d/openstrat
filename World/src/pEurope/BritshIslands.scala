/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pEarth
package pEurope
import geom._, WTile._

object OuterHebrides extends Area2("OuterHebrides", 57.83 ll -6.09, plain)
{
   val nLewis = 58.51 ll -6.26
   val swLewis = 57.94 ll -6.47
   val sHarris = 57.73 ll -6.97
   val sandray = 56.88 ll -7.50
   val wUist = 57.60 ll -7.53
   val wLewis = 58.12 ll -7.13
   
   val latLongs = LatLongs(nLewis, swLewis, sHarris, sandray, wUist, wLewis)
}

object Ireland extends Area2("Ireland", 53.36 ll -7.63, plain)
{
   val irelandSE = 52.17 ll -6.36
   val baltimore = 51.47 ll -9.37
   val dunquin = 52.11 ll -10.45
   val oranmore = 53.27 ll -8.93
   val carrowteige = 53.32 ll -9.85
   val ardoone = 54.30 ll -10.00
   val derkmorePoint = 54.27 ll -8.65
   val malinBeg = 54.66 ll -8.79
   val irelandN = 55.38 ll -7.37
   val torHead = 55.19 ll -6.06
   val nIrelandE = 54.48 ll -5.43
   val dundalk = 54.01 ll -6.34
   
   val latLongs = LatLongs(irelandSE, baltimore, dunquin, oranmore, carrowteige, ardoone, derkmorePoint, malinBeg,
         irelandN, torHead, nIrelandE, dundalk)         
}

object Shetland extends Area2("Shetland", 60.34 ll -1.23, plain)
{
   val south = 59.85 ll -1.27
   val sSoundsound = 60.20 ll -1.34
   val sSilwick = 60.14 ll -1.46
   val papaStour = 60.33 ll -1.73
   val eshaness = 60.48 ll -1.63
   val nUnst = 60.84 ll -0.87
   val eWhalsay = 60.38 ll -0.90
   
   val latLongs = LatLongs(south, sSoundsound, sSilwick, papaStour, eshaness, nUnst, eWhalsay)
}
