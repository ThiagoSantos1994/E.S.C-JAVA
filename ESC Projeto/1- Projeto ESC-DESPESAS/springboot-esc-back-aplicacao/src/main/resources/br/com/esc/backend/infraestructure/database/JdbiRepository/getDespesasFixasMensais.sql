SELECT
    id_Despesa,
    ds_Descricao,
    vl_Total,
    tp_Status,
    ds_Mes,
    ds_Ano,
    id_Funcionario,
    id_Ordem,
    tpFixasObrigatorias,
    tp_DespesaDebitoCartao,
    id_DetalheDespesaDebitoCartao
FROM
    tbd_DespesasFixasMensais
WHERE
    ds_Mes = :dsMes
    AND ds_Ano = :dsAno
    AND id_Despesa <> 0
    AND id_Funcionario =:idFuncionario
ORDER BY
    id_Despesa,id_Ordem
