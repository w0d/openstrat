/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pEarth
import geom._, WTile._

object Majorca extends Area2("Majorca", 39.59 ll 3.01, plain)
{
   val south = 39.26 ll 3.05
   val palma = 39.56 ll 2.63
   val portalsVells = 39.45 ll 2.51
   val santElm = 39.59 ll 2.34
   val capFormentor = 39.96 ll 3.21
   val east = 39.71 ll 3.47
   
   val latLongs = LatLongs(south, palma, portalsVells, santElm, capFormentor, east)
}

object Sicily extends Area2("Sicily", cen = 37.58 ll 14.27, plain)
{   
   val sSicily = 36.66 ll 15.08
   val kartibubbo = 37.56 ll 12.67
   val marsala = 37.80 ll 12.42
   val calaRossa = 38.18 ll 12.73
   val mondello = 38.22 ll 13.32
   val n1 =37.97 ll 13.74
   val torreFaro = 38.26 ll 15.65
   val contradoFortino = 38.24 ll 15.58
   val messina = 38.18 ll 15.56
   val catania = 37.48 ll 15.08
   
   val latLongs = LatLongs(sSicily, kartibubbo, marsala, calaRossa, mondello, n1, torreFaro, contradoFortino, messina, catania)   
}


object Canarias extends Area2("Canarias", 27.96 ll -15.60, plain)
{   
   val elHierro = 27.72 ll -18.15
   val laPalma = 28.85 ll -17.92
   val lanzarote = 29.24 ll -13.47
   val fuerteventura = 28.24 ll -13.94
   val granCanaria = 27.74 ll -15.60
   
   val latLongs = LatLongs(elHierro, laPalma, lanzarote, fuerteventura, granCanaria)
}

object SaharaWest extends Area2("NWAfrica", 25 ll 1, desert)
{
   val southLine = 17.north
   val eastLine = 16.75.east
   val northEast = 31.2.north *  eastLine
   
   val southWest = southLine * 16.27.west
   val nouakchott = 18.078 ll -16.02
   val nouadhibouBay = 21.28 ll -16.90
   val nouadhibou = 20.77 ll -17.05
   val nou2 = 21.27 ll -17.04
   val boujdour = 26.13 ll -14.50
   val agadir = 30.16 ll -9.24   
   val rabat = 34.03 ll -6.83
   val tangierW = 35.79 ll -5.92
   val ceuta = 35.88 ll -5.31
   val alHoceima = 35.15 ll -4.38
   val tunis = 37.08 ll 10.20
   val neTunis = 37.07 ll 11.04
   val sTunis = 33.30 ll 10.08
   val misrata = 32.37 ll 15.03
   val southEast = southLine * eastLine
   
   val latLongs = LatLongs(
         southWest, nouakchott,nouadhibouBay, nouadhibou, nou2, boujdour, agadir, rabat,
         tangierW, ceuta, alHoceima, tunis, neTunis, sTunis, misrata, northEast, southEast)  
}

object AfricaMidWest extends Area2("WAfrica", 11 ll 0, plain)
{
   //val wAfricaE = 16.75.east 
   val cAfricaN = 4.53.north
   val cAfricaNW = cAfricaN * 8.89.east
   val sangana = 4.31 ll 5.99
   val aiyetoro = 6.20 ll 4.66
   val capeThreePoints = 4.73 ll -2.09
   val liberia = 4.18 ll -7.22
   val sierraLeone = 8.11 ll -13.11
   val dakar = 14.30 ll -17.2
   val keurMassene = 16.7 ll -16.38
   val southEast =  cAfricaN * SaharaWest.eastLine
   
   val latLongs = LatLongs(cAfricaNW, sangana, aiyetoro, capeThreePoints,
         liberia, sierraLeone, dakar, keurMassene, SaharaWest.southWest, SaharaWest.southEast, southEast, cAfricaNW)
}

