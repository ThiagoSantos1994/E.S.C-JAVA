SELECT
    MAX(id_Despesa) as idDespesaTemp
FROM
    tbd_DespesasFixasMensaisTemp
WHERE
    id_Funcionario = :idFuncionario