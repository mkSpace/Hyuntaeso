package candidate

class CandidateArray : ArrayList<Candidate> {
    constructor() : super()
    constructor(collection: Collection<Candidate>) : super(collection)
    constructor(initialCapacity: Int) : super(initialCapacity)
}