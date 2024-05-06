package pet_store;

public class AdoptionRequestWrapper {
    private String petName;
    private String requesterName;
    private String requesterAddress;
    private int requesterPhone;
    private String requestMessage;
    private String ownerName;
    private int ownerPhone;
    private String ownerAddress;
    
    
    
    
	public AdoptionRequestWrapper(String petName, String requesterName, String requesterAddress, int requesterPhone, String requestMessage) {
		this.petName = petName;
		this.requesterName = requesterName;
		this.requesterAddress = requesterAddress;
		this.requesterPhone = requesterPhone;
		this.requestMessage = requestMessage;
	}
	public AdoptionRequestWrapper(String petName, String ownerName, int ownerPhone, String ownerAddress, String requestMessage) {
		this.petName = petName;
		this.ownerName = ownerName;
		this.ownerAddress = ownerAddress;
		this.ownerPhone = ownerPhone;
		this.requestMessage = requestMessage;
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
