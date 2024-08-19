INSERT INTO tbd_DetalheDespesasMensaisLogs
    (
        id_Despesa,
        id_DetalheDespesa,
        id_Ordem,
        id_Funcionario,
        vl_ValorAlterado,
        ds_DataHoraAlteracao
    )
VALUES (
    :idDespesa,
    :idDetalheDespesa,
    :idOrdem,
    :idFuncionario,
    :valorCampo,
    GETDATE()
)
