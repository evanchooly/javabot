

class ChannelController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = []

    def list = {
//        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
      def c = Channel.createCriteria()
      def results = c {
        eq("logged", true)
        order(name)
      }
§
        [ channelInstanceList: results, channelInstanceTotal: results.size() ]
    }

    def show = {
        def channelInstance = Channel.get( params.id )

        if(!channelInstance) {
            flash.message = "Channel not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ channelInstance : channelInstance ] }
    }

    def delete = {
        def channelInstance = Channel.get( params.id )
        if(channelInstance) {
            try {
                channelInstance.delete()
                flash.message = "Channel ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Channel ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Channel not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def channelInstance = Channel.get( params.id )

        if(!channelInstance) {
            flash.message = "Channel not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ channelInstance : channelInstance ]
        }
    }

    def update = {
        def channelInstance = Channel.get( params.id )
        if(channelInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(channelInstance.version > version) {
                    
                    channelInstance.errors.rejectValue("version", "channel.optimistic.locking.failure", "Another user has updated this Channel while you were editing.")
                    render(view:'edit',model:[channelInstance:channelInstance])
                    return
                }
            }
            channelInstance.properties = params
            if(!channelInstance.hasErrors() && channelInstance.save()) {
                flash.message = "Channel ${params.id} updated"
                redirect(action:show,id:channelInstance.id)
            }
            else {
                render(view:'edit',model:[channelInstance:channelInstance])
            }
        }
        else {
            flash.message = "Channel not found with id ${params.id}"
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def channelInstance = new Channel()
        channelInstance.properties = params
        return ['channelInstance':channelInstance]
    }

    def save = {
        def channelInstance = new Channel(params)
        if(!channelInstance.hasErrors() && channelInstance.save()) {
            flash.message = "Channel ${channelInstance.id} created"
            redirect(action:show,id:channelInstance.id)
        }
        else {
            render(view:'create',model:[channelInstance:channelInstance])
        }
    }
}
