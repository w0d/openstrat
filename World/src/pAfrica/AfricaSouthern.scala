/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pEarth
import geom._, LatLong._, WTile._

object AfricaSouthern extends Area1("AfricaSouthern", -16.14 ll 24.36)
{
  type A2Type = Area2
  override def fill = true//false
  import AfricaSouthernPts._
  override val a2Arr: Arr[Area2] = Arr(sAfrica, cAfrica, sEAfrica, madagascar)
  //override val gridMaker = E80Empty
}

object AfricaSouthernPts
{
  val cAfricaN = 4.42.north
  val sAfricaNW = - 17 ll 11.76
  val baiaFarta = -12.81 ll 13.01
  val luanda = -8.35 ll 13.15
  val bouemba = 2.09 ll 9.76
  val wAfricaEquator = 0.0 ll 9.13
  val katongaMouth =  -0.14 ll 31.94
  val lakeVictoriaSW = -2.64 ll 31.76
  val sAfricaN = 17.south
  val cAfricaSE = sAfricaN * 31.east
  val cAfrica =  Area2("CAfrica", -7 ll 25, jungle, sAfricaNW, baiaFarta, luanda, wAfricaEquator, bouemba, AfricaMidWest.cAfricaNW,
    AfricaMidWest.southEast, AfricaNorthEast.cAfricaNE, katongaMouth, lakeVictoriaSW, cAfricaSE)
   
  val lakeVictoriaSE = -2.23 ll 33.84
  val lakeVictoriaE = -0.39 ll 34.26
  val lakeVictoriaN = 0.34 ll 33.34
  val eAfricaEquator = 0.0 ll 42.4
  val mombassa = -4.03 ll 39.28
  val seNacala = -14.4 ll 40.3
  val sAfricaNE = -17 ll 39.06
  val sEAfrica = Area2("SEAfrica", -2.17 ll 36.64, plain, cAfricaSE, lakeVictoriaSW, lakeVictoriaSE, lakeVictoriaE, lakeVictoriaN, katongaMouth,
    AfricaNorthEast.cAfricaNE, AfricaNorthEast.southEast, eAfricaEquator, mombassa, seNacala, sAfricaNE)
   
  val agulhas = degs(-34.83, 20.00)
  val capeTown = degs(-34, 19)
  val nNamibia = -17.12 ll 11.3
  val beira = -19.35 ll 34.3
  val inhambane = -23.38 ll 35.2
  val maputo = -25.4 ll 32.2
  val richardsBay = degs(-29, 32)
  val portLiz = degs(-34, 26)
         
  val sAfrica = Area2("SAfrica", -25 ll 24, plain, agulhas, capeTown, nNamibia, sAfricaNW, cAfricaSE, sAfricaNE, beira, inhambane, maputo,
    richardsBay, portLiz)
         
  val madagascarN = degs(-11.95, 49.26)
  val madagascarE = degs(-15.33, 50.48)
  val madagascarSE = degs(-25.03, 46.99)
  val madagascarS = degs(-25.60, 45.16)
  val tambohorano = degs(-17.51, 43.93)
  val madagascar = Area2("Madagascar", degs(-19.42, 46.57), plain, madagascarN, madagascarE, madagascarSE, madagascarS, tambohorano)
}
