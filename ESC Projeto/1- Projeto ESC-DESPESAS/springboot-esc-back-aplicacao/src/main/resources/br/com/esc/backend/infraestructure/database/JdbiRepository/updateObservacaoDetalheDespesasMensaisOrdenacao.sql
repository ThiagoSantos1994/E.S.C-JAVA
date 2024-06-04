UPDATE
    tbd_ObservacaoDetalheDespesaMensal
SET
    id_Ordem = :idNovaOrdem
WHERE
    id_Despesa = :idDespesa
    AND id_DetalheDespesa = :idDetalheDespesa
    AND id_Ordem = :idOrdem
    AND id_Funcionario = :idFuncionario
