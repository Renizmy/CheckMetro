package com.example.checkmetro.service


import retrofit2.http.GET
import retrofit2.http.Path


interface APIRATP {
    @GET("v4/lines/metros")
    suspend fun getLines(): ApiLines

    @GET("v4/stations/metros/{code_line}")
    suspend fun getStations(@Path("code_line") code: String) : ApiStations

    @GET("v4/schedules/metros/{code_line}/{station}/A%2BR")
    suspend fun getSchedulesTrains(
        @Path("code_line") code: String,
        @Path("station") station: String ) : ApiSchedules

    @GET("v4/traffic/metros/")
    suspend fun getMetroTraffic() : ApiTraffic

    @GET("v4/traffic/metros/{code_line}")
    suspend fun getLineTraffic(@Path("code_line") code : String) : ApiLineTraffic


}

data class ApiLines(
    val result: ResultLines
)

data class ResultLines(val metros: List<Metros> = emptyList())

data class Metros(
    val code: String,
    val name: String,
    val directions: String,
    val id: Int
)

data class ApiStations(
    val result: ResultStations
)

data class ResultStations(
    val stations: List<Stations>
)

data class Stations(
    val name: String,
    val slug: String
)

data class ApiSchedules (
    val result : ResultSchedules
)
data class ResultSchedules (
    val schedules : List<Schedules>
)
data class Schedules (
    val message : String,
    val destination : String
)


data class ApiTraffic(
    val result:ResultTraffic
)
data class ResultTraffic(
    val metros:List<MetrosTraffic>
)
data class MetrosTraffic(
    val line : String,
    val slug : String,
    val title : String,
    val message : String
)


data class ApiLineTraffic(
    val result:MetrosTraffic
)

data class ResultLineTraffic(
    val metros:MetrosTraffic
)