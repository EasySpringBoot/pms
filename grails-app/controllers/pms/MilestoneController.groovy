package pms

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class MilestoneController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Milestone.list(params), model:[milestoneCount: Milestone.count()]
    }

    def show(Milestone milestone) {
        respond milestone
    }

    def create() {
        respond new Milestone(params)
    }

    @Transactional
    def save(Milestone milestone) {
        if (milestone == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (milestone.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond milestone.errors, view:'create'
            return
        }

        milestone.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'milestone.label', default: 'Milestone'), milestone.id])
                redirect milestone
            }
            '*' { respond milestone, [status: CREATED] }
        }
    }

    def edit(Milestone milestone) {
        respond milestone
    }

    @Transactional
    def update(Milestone milestone) {
        if (milestone == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (milestone.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond milestone.errors, view:'edit'
            return
        }

        milestone.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'milestone.label', default: 'Milestone'), milestone.id])
                redirect milestone
            }
            '*'{ respond milestone, [status: OK] }
        }
    }

    @Transactional
    def delete(Milestone milestone) {

        if (milestone == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        milestone.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'milestone.label', default: 'Milestone'), milestone.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'milestone.label', default: 'Milestone'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
