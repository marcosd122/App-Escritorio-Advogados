package projeto_escritorio;

public class Cliente {

    private String nome_Cliente;
    private int processo_Cliente;
    private String audiencia_Cliente;
    private int cpf_Cliente;
    private int dtnasc_Cliente;
    private String estCivil_Cliente;
    private String email_Cliente;
    private int tel1_Cliente;

    private int CEP_Cliente;

    private String end_Cliente;
    private String comp_Cliente;
    private int numero_Cliente;
    private String bairro_Cliente;
    private String cidade_Cliente;
    private String estado_Cliente;

    private byte[] picture;

    public Cliente(String nome_Cliente, int processo_Cliente, int cpf_Cliente,
            int dtnasc_Cliente, String estCivil_Cliente, String email_Cliente, int tel1_Cliente,
            int CEP_Cliente, String end_Cliente, String comp_Cliente,
            int numero_Cliente, String bairro_Cliente, String cidade_Cliente,
            String estado_Cliente, byte[] image) {

        this.nome_Cliente = nome_Cliente;
        this.processo_Cliente = processo_Cliente;
        this.audiencia_Cliente = audiencia_Cliente;
        this.cpf_Cliente = cpf_Cliente;
        this.dtnasc_Cliente = dtnasc_Cliente;
        this.estCivil_Cliente = estCivil_Cliente;
        this.email_Cliente = email_Cliente;
        this.tel1_Cliente = tel1_Cliente;

        this.CEP_Cliente = CEP_Cliente;

        this.end_Cliente = end_Cliente;
        this.comp_Cliente = comp_Cliente;
        this.numero_Cliente = numero_Cliente;
        this.bairro_Cliente = bairro_Cliente;
        this.cidade_Cliente = cidade_Cliente;
        this.estado_Cliente = estado_Cliente;

        this.picture = image;

    }

    public String getnome_Cliente() {
        return nome_Cliente;
    }

    public int getprocesso_Cliente() {
        return processo_Cliente;
    }

    public String getaudiencia_Cliente() {
        return audiencia_Cliente;
    }

    public int getcpf_Cliente() {
        return cpf_Cliente;
    }

    public int getdtnasc_Cliente() {
        return dtnasc_Cliente;
    }

    public String getestCivil_Cliente() {
        return estCivil_Cliente;
    }

    public String getemail_Cliente() {
        return email_Cliente;
    }

    public int gettel1_Cliente() {
        return tel1_Cliente;
    }

    public int getCEP_Cliente() {
        return CEP_Cliente;
    }

    public String getend_Cliente() {
        return end_Cliente;
    }

    public String getcomp_Cliente() {
        return comp_Cliente;
    }

    public int getnumero_Cliente() {
        return numero_Cliente;
    }

    public String getbairro_Cliente() {
        return bairro_Cliente;

    }

    public String getcidade_Cliente() {
        return cidade_Cliente;
    }

    public String getestado_Cliente() {
        return estado_Cliente;
    }

    public byte[] getImage() {
        return picture;
    }

}
