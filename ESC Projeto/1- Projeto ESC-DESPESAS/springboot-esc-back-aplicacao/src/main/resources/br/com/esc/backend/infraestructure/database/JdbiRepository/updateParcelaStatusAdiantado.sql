UPDATE
    tbd_Parcelas
SET
    tp_Baixado = 'S',
    tp_ParcelaAdiada = 'S',
    ds_Observacoes = :dsObservacoes,
    vl_Parcela = '0,00',
    vl_Desconto = '-',
    id_Despesa = :idDespesa,
    id_DetalheDespesa = :idDetalheDespesa
WHERE
   id_DespesaParcelada = :idDespesaParcelada
   AND id_Parcelas = :idParcela
   AND id_Funcionario = :idFuncionario