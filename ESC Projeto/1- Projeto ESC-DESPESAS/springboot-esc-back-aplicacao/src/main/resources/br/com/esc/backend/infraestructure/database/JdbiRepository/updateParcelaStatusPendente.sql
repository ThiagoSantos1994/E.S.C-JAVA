UPDATE
    tbd_Parcelas
SET
    tp_Baixado = 'N',
    tp_Quitado = 'N',
    ds_Observacoes = '',
    id_DetalheDespesa = 0,
    id_Despesa = 0
WHERE
   id_Despesa = :idDespesa
   AND id_DetalheDespesa = :idDetalheDespesa
   AND (:idDespesaParcelada IS NULL OR id_DespesaParcelada = :idDespesaParcelada)
   AND (:idParcela IS NULL OR id_Parcelas = :idParcela)
   AND id_Funcionario = :idFuncionario
   AND tp_ParcelaAdiada = 'N'