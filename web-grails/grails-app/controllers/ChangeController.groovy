class ChangeController {
  static transactional = false

  def list = {
    params.max = Math.min(params.max ? params.max.toInteger() : 100, 100)
    params.offset = params.offset ? params.offset.toInteger() : 0
    def c = Change.createCriteria()
    def results = c {
      order("changeDate", "desc")
      maxResults(params.max)
      firstResult(params.offset)
    }
    [changeInstanceList: results, changeInstanceTotal: Change.count()]
  }

  def show = { redirect(action: list, params: params) }
  def index = { redirect(action: list, params: params) }
  def delete = { redirect(action: list, params: params) }
  def edit = { redirect(action: list, params: params) }
  def update = { redirect(action: list, params: params) }
  def create = { redirect(action: list, params: params) }
  def save = { redirect(action: list, params: params) }
}
