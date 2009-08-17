class LogController {
  def list = {
    if (params.date == null) {
      params.date = new Date().format("yyyy-MM-dd")
    }
    params.logDay = Date.parse("yyyy-MM-dd", params.date)
    params.prevLogDay = params.logDay.previous()
    params.nextLogDay = params.logDay.next()
    def c = Log.createCriteria()
    def results
    def Channel[] channel = Channel.findByName(params.channel)
    if (channel != null && channel.size() != 0 && channel[0].logged) {
      results = c {
        eq("channel", params.channel)
        and {
          between("updated", params.logDay, params.nextLogDay)
        }
        order("updated", "asc")
      }
    } else {
      results = []
    }
    [logInstanceList: results, logInstanceCount: results.size()]
  }

  def index  = { redirect(action: list, params: params) }
  def show   = { redirect(action: list, params: params) }
  def delete = { redirect(action: list, params: params) }
  def edit   = { redirect(action: list, params: params) }
  def update = { redirect(action: list, params: params) }
  def create = { redirect(action: list, params: params) }
  def save   = { redirect(action: list, params: params) }
}