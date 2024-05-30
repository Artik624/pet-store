package pet_store;

/**
 * A wrapper class to retrieve adoption requests by User or for User
 * @author Artiom Cooper
 */
public class AdoptionRequestWrapper {
    private String petName;
    private String petId;
    private String requesterName;
    private String requesterAddress;
    private int requesterPhone;
    private String requestMessage;
    private String ownerName;
    private int ownerPhone;
    private String ownerAddress;
    
    
    
    /**
     * Constructor for requester by user
     * @param petName
     * @param requesterName
     * @param requesterAddress
     * @param requesterPhone
     * @param requestMessage
     */
	public AdoptionRequestWrapper(String petName, String requesterName, String requesterAddress, int requesterPhone, String requestMessage) {
		this.petName = petName;
		this.requesterName = requesterName;
		this.requesterAddress = requesterAddress;
		this.requesterPhone = requesterPhone;
		this.requestMessage = requestMessage;
	}
	
	/**
	 * Constructor for requests for user
	 * @param petName
	 * @param ownerName
	 * @param ownerPhone
	 * @param ownerAddress
	 * @param requestMessage
	 * @param pet_id
	 */
	public AdoptionRequestWrapper(String petName, String ownerName, int ownerPhone, String ownerAddress, String requestMessage, String pet_id) {
		this.petName = petName;
		this.ownerName = ownerName;
		this.ownerAddress = ownerAddress;
		this.ownerPhone = ownerPhone;
		this.requestMessage = requestMessage;
		this.petId = pet_id;
	}
	
	//Getter & Setters
	public String getPetId() {
		return petId;
	}
	public void setPetId(String petId) {
		this.petId = petId;
	}
	public String getPetName() {
		return petName;
	}
	public String getRequesterName() {
		return requesterName;
	}
	public String getRequesterAddress() {
		return requesterAddress;
	}
	public int getRequesterPhone() {
		return requesterPhone;
	}
	public String getRequestMessage() {
		return requestMessage;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public int getOwnerPhone() {
		return ownerPhone;
	}
	public String getOwnerAddress() {
		return ownerAddress;
	}
}
