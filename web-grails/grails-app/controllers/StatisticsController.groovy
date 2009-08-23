import dto.Statistics
import groovy.sql.Sql
import java.math.RoundingMode

class StatisticsController {
  def index = { redirect(action: list, params: params) }

  def dataSource;

  def list = {Statistics stats ->
    println("\n\n\n\n\n\n\n\n")
    def sql = new Sql(dataSource)
    def channels = sql.rows("select name from channel where logged is true").collect { it.name }
    if (!stats.table) {
      stats.table = channels.size > 0 ? channels[0].toString() : null
    }
    println(stats.table)
/*
    println("\n\n\n\n\n\n\n\nchannels = ${channels}")
    def clause = channels.inject("", {str, item -> str + (str.length() != 0 ? ", " : "") + "'${item}'"})
    println("clause = ${clause}")
    def query = "select count(*) from logs where channel in (${clause})"
    println ("query = ${query}")
    def count = sql.rows(query).count
    println("count = ${count}")
*/
    def start = null
    def end = null
    if (stats.table) {
      def data = sql.rows("select count(*), max(updated), min(updated) from logs where channel=${stats.table}")
      stats.count = data.count[0]
      stats.start = data.min[0].format("yyyy-MM-dd")
      stats.end = data.max[0].format("yyyy-MM-dd")
      def results = sql.rows("select nick, count(*) as entries from logs where channel=${stats.table} group by nick order by 2 desc limit 25")
      results.each {
        stats.users.add(["nick" : it.nick, "count" : it.entries, "percent" : (it.entries * 100.0 / stats.count).setScale(2, RoundingMode.HALF_UP)])
      }
    }
    return ["stats": stats, "channels": channels]
  }
  // the delete, save and update actions only accept POST requests
  static allowedMethods = [delete: 'POST', save: 'POST', update: 'POST']

  def show = {
    def statisticsInstance = Statistics.get(params.id)

    if (!statisticsInstance) {
      flash.message = "Statistics not found with id ${params.id}"
      redirect(action: list)
    } else {
      return [statisticsInstance: statisticsInstance]
    }
  }
}
