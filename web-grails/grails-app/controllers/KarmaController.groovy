class KarmaController {
  static transactional = false

  def list = {
    params.max = Math.min(params.max ? params.max.toInteger() : 100, 100)
    params.offset = params.offset ? params.offset.toInteger() : 0
    def c = Karma.createCriteria()
    def results = c {
      order(params.sort ? params.sort : "value", params.order ? params.order : "desc")
      maxResults(params.max)
      firstResult(params.offset)
    }
    [karmaInstanceList: results, karmaInstanceTotal: Karma.count()]
  }

  def show = { redirect(action: list, params: params) }
  def index = { redirect(action: list, params: params) }
  def delete = { redirect(action: list, params: params) }
  def edit = { redirect(action: list, params: params) }
  def update = { redirect(action: list, params: params) }
  def create = { redirect(action: list, params: params) }
  def save = { redirect(action: list, params: params) }
}
