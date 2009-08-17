class FactoidController {
  def list = {
    params.max = Math.min(params.max ? params.max.toInteger() : 100, 100)
    params.offset = params.offset ? params.offset.toInteger() : 0
    def c = Factoid.createCriteria()
    def results = c {
      order(params.sort ? params.sort : "name", params.order ? params.order : "asc")
      maxResults(params.max)
      firstResult(params.offset)
    }
    [factoidInstanceList: results, factoidInstanceTotal: Factoid.count()]
  }

  def show = {
    def factoidInstance = Factoid.get(params.id)

    if (!factoidInstance) {
      flash.message = "Factoid not found with id ${params.id}"
      redirect(action: list)
    }
    else { return [factoidInstance: factoidInstance] }
  }

  def index = { redirect(action: list, params: params) }
  def delete = { redirect(action: list, params: params) }
  def edit = { redirect(action: list, params: params) }
  def update = { redirect(action: list, params: params) }
  def create = { redirect(action: list, params: params) }
  def save = { redirect(action: list, params: params) }}
