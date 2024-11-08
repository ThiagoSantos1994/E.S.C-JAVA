INSERT INTO tbd_DetalheDespesasMensaisLogs
    (
        id_Despesa,
        id_DetalheDespesa,
        id_Funcionario,
        ds_LogDespesa
    )
VALUES (
    :idDespesa,
    :idDetalheDespesa,
    :idFuncionario,
    :dsLogDespesa
)
