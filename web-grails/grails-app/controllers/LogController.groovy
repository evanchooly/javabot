class LogController {
  def index = { redirect(action: list, params: params) }

  // the delete, save and update actions only accept POST requests
  static allowedMethods = [/*delete:'POST', save:'POST', update:'POST'*/]

  def list = {
    params.max = Math.min(params.max ? params.max.toInteger() : 10, 100)
    [logInstanceList: Log.list(params), logInstanceTotal: Log.count()]
  }

  def show = {
    def logInstance = Log.get(params.id)

    if (!logInstance) {
      flash.message = "Log not found with id ${params.id}"
      redirect(action: list)
      return
    }
    else { return [logInstance: logInstance] }
  }

  def delete = {
    redirect(action: list, params: params)
  }

  def edit = {
    redirect(action: list, params: params)
  }

  def update = {
    def logInstance = Log.get(params.id)
    if (logInstance) {
      if (params.version) {
        def version = params.version.toLong()
        if (logInstance.version > version) {
          logInstance.errors.rejectValue("version", "log.optimistic.locking.failure", "Another user has updated this Log while you were editing.")
          render(view: 'edit', model: [logInstance: logInstance])
          return
        }
      }
      logInstance.properties = params
      if (!logInstance.hasErrors() && logInstance.save()) {
        flash.message = "Log ${params.id} updated"
        redirect(action: show, id: logInstance.id)
      }
      else {
        render(view: 'edit', model: [logInstance: logInstance])
      }
    }
    else {
      flash.message = "Log not found with id ${params.id}"
      redirect(action: edit, id: params.id)
    }
  }

  def create = {
    redirect(action: list, params: params)
  }

  def save = {
    redirect(action: list, params: params)
  }
}