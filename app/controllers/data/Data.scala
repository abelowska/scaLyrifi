package controllers.data

case class SongHeader(title: String, author: String)

case class Song(header: SongHeader, content: String)
