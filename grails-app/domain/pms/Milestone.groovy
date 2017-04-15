package pms

class Milestone {

    static constraints = {
    }

    Integer id
    Integer projectId
    String name
    String owner
    Date expectDate
    Date actualDate
    String status

}
