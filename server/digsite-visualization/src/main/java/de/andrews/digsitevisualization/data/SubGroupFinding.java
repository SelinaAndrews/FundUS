package de.andrews.digsitevisualization.data;

public class SubGroupFinding {

    private String identification;
    private String basicForm;
    private String definition;
    private String geologicalHorizon;
    private String id;
    private String image;
    private String comment;
    private boolean worked;

    public SubGroupFinding() {}

    public SubGroupFinding(String identification, String basicForm, String definition, String geologicalHorizon, String id, String image, String comment, boolean worked) {
        this.identification = identification;
        this.basicForm = basicForm;
        this.definition = definition;
        this.geologicalHorizon = geologicalHorizon;
        this.id = id;
        this.image = image;
        this.comment = comment;
        this.worked = worked;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getBasicForm() {
        return basicForm;
    }

    public void setBasicForm(String basicForm) {
        this.basicForm = basicForm;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getGeologicalHorizon() {
        return geologicalHorizon;
    }

    public void setGeologicalHorizon(String geologicalHorizon) {
        this.geologicalHorizon = geologicalHorizon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isWorked() {
        return worked;
    }

    public void setWorked(boolean worked) {
        this.worked = worked;
    }
}
