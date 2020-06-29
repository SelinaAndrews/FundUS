package de.andrews.digsitevisualization.data;

public class SingularFinding {

    private Vertex vertex;
    private int layer;
    private String geologicalHorizon;
    private String identification;
    private String basicForm;
    private String definition;
    private String id;
    private String image;
    private String comment;
    private boolean worked;

    public SingularFinding() {}

    public SingularFinding(Vertex vertex, int layer, String geologicalHorizon, String identification, String basicForm, String definition, String id, String image, String comment, boolean worked) {
        this.vertex = vertex;
        this.layer = layer;
        this.geologicalHorizon = geologicalHorizon;
        this.identification = identification;
        this.basicForm = basicForm;
        this.definition = definition;
        this.id = id;
        this.image = image;
        this.comment = comment;
        this.worked = worked;
    }

    public Vertex getVertex() {
        return vertex;
    }

    public void setVertex(Vertex vertex) {
        this.vertex = vertex;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public String getGeologicalHorizon() {
        return geologicalHorizon;
    }

    public void setGeologicalHorizon(String geologicalHorizon) {
        this.geologicalHorizon = geologicalHorizon;
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
